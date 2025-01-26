package org.pamdesa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pamdesa.annotation.MetaData;
import org.pamdesa.annotation.MetaDatas;
import org.pamdesa.model.exception.ClientException;
import org.pamdesa.model.exception.JwtAuthenticationException;
import org.pamdesa.payload.response.Response;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.lang.reflect.Field;
import java.util.*;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ErrorController {

  private final MessageSource messageSource;

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public Response<Object> httpMessageNotReadableException(HttpMessageNotReadableException e) {
    log.warn(HttpMessageNotReadableException.class.getName(), e);
    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.BAD_REQUEST.value());
    response.setStatus(HttpStatus.BAD_REQUEST.name());
    return response;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ClientException.class)
  public Response<Object> clientException(ClientException e) {
    log.warn(ClientException.class.getName(), e);
    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.BAD_REQUEST.value());
    response.setStatus(e.getMessage());
    return response;
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Throwable.class)
  public Response<Object> throwable(Throwable e) {
    String uuid = UUID.randomUUID().toString();
    log.error("errorClass: {}, errorId: {}", e.getClass().getName(), uuid, e);
    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
    response.setData(uuid);
    return response;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ServerWebInputException.class)
  public ResponseEntity<Response<Object>> serverWebInputException(ServerWebInputException e) {
    log.warn(ServerWebInputException.class.getName(), e);

    Map<String, List<String>> errors = new HashMap<>();
    if (e.getMethodParameter() != null) {
      errors.put(e.getMethodParameter().getParameterName(), Collections.singletonList(e.getReason()));
    }

    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.BAD_REQUEST.value());
    response.setStatus(HttpStatus.BAD_REQUEST.name());
    response.setErrors(errors);

    return ResponseEntity.status(e.getStatus()).body(response);
  }

  @ExceptionHandler(JwtAuthenticationException.class)
  public ResponseEntity<String> handleJwtAuthenticationException(JwtAuthenticationException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<Response<Object>> webExchangeBindException(WebExchangeBindException e) {
    log.warn(WebExchangeBindException.class.getName(), e);
    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.BAD_REQUEST.value());
    response.setStatus(HttpStatus.BAD_REQUEST.name());
    response.setErrors(from(e.getBindingResult(), messageSource));

    return ResponseEntity.status(e.getStatus()).body(response);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Response<Object>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.warn(MethodArgumentNotValidException.class.getName(), e);
    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.BAD_REQUEST.value());
    response.setStatus(HttpStatus.BAD_REQUEST.name());
    response.setErrors(from(e.getBindingResult(), messageSource));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Response<Object>> constraintViolationException(ConstraintViolationException e) {
    log.warn(ConstraintViolationException.class.getName(), e);

    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.BAD_REQUEST.value());
    response.setStatus(HttpStatus.BAD_REQUEST.name());
    response.setErrors(from(e.getConstraintViolations()));
    response.setMetadata(Collections.singletonMap("errors", getMetaData(e.getConstraintViolations())));

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Response<Object>> responseStatusException(ResponseStatusException e) {
    log.warn(ResponseStatusException.class.getName(), e);
    Map<String, List<String>> errors = new HashMap<>();
    errors.put("reason", Collections.singletonList(e.getReason()));

    Response<Object> response = new Response<>();
    response.setCode(e.getStatus().value());
    response.setStatus(e.getStatus().name());
    response.setErrors(errors);

    return ResponseEntity.status(e.getStatus()).body(response);
  }

  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler(MediaTypeNotSupportedStatusException.class)
  public Response<Object> mediaTypeNotSupportedException(MediaTypeNotSupportedStatusException e) {
    log.warn(MediaTypeNotSupportedStatusException.class.getName(), e);
    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    response.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.name());
    return response;
  }

  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  @ExceptionHandler(NotAcceptableStatusException.class)
  public Response<Object> notAcceptableStatusException(NotAcceptableStatusException e) {
    log.warn(NotAcceptableStatusException.class.getName(), e);
    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.NOT_ACCEPTABLE.value());
    response.setStatus(HttpStatus.NOT_ACCEPTABLE.name());
    return response;
  }

  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
  public Response<Object> unsupportedMediaTypeStatusException(UnsupportedMediaTypeStatusException e) {
    log.warn(UnsupportedMediaTypeStatusException.class.getName(), e);
    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    response.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.name());
    return response;
  }

  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(MethodNotAllowedException.class)
  public Response<Object> methodNotAllowedException(MethodNotAllowedException e) {
    log.warn(MethodNotAllowedException.class.getName(), e);
    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.METHOD_NOT_ALLOWED.value());
    response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.name());
    return response;
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(ServerErrorException.class)
  public Response<Object> serverErrorException(ServerErrorException e) {
    log.warn(ServerErrorException.class.getName(), e);
    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
    return response;
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(BadCredentialsException.class)
  public Response<Object> handleBadCredentialsException(BadCredentialsException ex) {
    log.warn(ServerErrorException.class.getName(), ex);
    Response<Object> response = new Response<>();
    response.setCode(HttpStatus.UNAUTHORIZED.value());
    response.setStatus(HttpStatus.UNAUTHORIZED.name());
    return response;
  }

  static Map<String, List<String>> from(BindingResult result, MessageSource messageSource) {
    return from(result, messageSource, Locale.getDefault());
  }

  static Map<String, List<String>> from(BindingResult result, MessageSource messageSource, Locale locale) {
    if (result.hasFieldErrors()) {
      Map<String, List<String>> map = new HashMap<>();

      for (FieldError fieldError : result.getFieldErrors()) {
        String field = fieldError.getField();

        if (!map.containsKey(fieldError.getField())) {
          map.put(field, new ArrayList<>());
        }

        String errorMessage = messageSource.getMessage(fieldError.getCode(), fieldError.getArguments(), fieldError.getDefaultMessage(), locale);
        map.get(field).add(errorMessage);
      }

      return map;
    } else {
      return Collections.emptyMap();
    }
  }

  static Map<String, List<String>> from(Set<ConstraintViolation<?>> constraintViolations) {
    Map<String, List<String>> map = new HashMap<>();

    constraintViolations.forEach(violation -> {
      for (String attribute : getAttributes(violation)) {
        putEntry(map, attribute, violation.getMessage());
      }
    });

    return map;
  }

  static void putEntry(Map<String, List<String>> map, String key, String value) {
    if (!map.containsKey(key)) {
      map.put(key, new ArrayList<>());
    }
    map.get(key).add(value);
  }

  static String[] getAttributes(ConstraintViolation<?> constraintViolation) {
    String[] values = (String[]) constraintViolation.getConstraintDescriptor().getAttributes().get("path");
    if (values == null || values.length == 0) {
      return getAttributesFromPath(constraintViolation);
    } else {
      return values;
    }
  }

  static String[] getAttributesFromPath(ConstraintViolation<?> constraintViolation) {
    Path path = constraintViolation.getPropertyPath();

    StringBuilder builder = new StringBuilder();
    path.forEach(node -> {
      if (node.getName() != null) {
        if (builder.isEmpty()) {
          builder.append(node.getName());
        }
        builder.append(".");
      }
    });

    return new String[]{builder.toString()};
  }

  public Map<String, Map<String, String>> getMetaData(Set<ConstraintViolation<?>> constraintViolations) {
    Map<String, Map<String, String>> metadata = new HashMap<>();
    constraintViolations.forEach(violation -> {
      try {
        Class<?> beanClass = violation.getLeafBean().getClass();

        String field = "";
        for (Path.Node node : violation.getPropertyPath()) {
          field = node.getName();
        }

        Field declaredField = beanClass.getDeclaredField(field);
        MetaDatas metaDatas = declaredField.getAnnotation(MetaDatas.class);

        if (metaDatas != null) {
          Map<String, String> values = new HashMap<>();

          for (MetaData metaData : metaDatas.value()) {
            values.put(metaData.key(), metaData.value());
          }

          for (String attribute : getAttributes(violation)) {
            metadata.put(attribute, values);
          }
        }

      } catch (Throwable throwable) {
        log.warn(throwable.getMessage(), throwable);
      }
    });

    return metadata;
  }

}
