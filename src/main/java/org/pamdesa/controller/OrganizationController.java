package org.pamdesa.controller;

import lombok.RequiredArgsConstructor;
import org.pamdesa.helper.ResponseHelper;
import org.pamdesa.model.constant.AppPath;
import org.pamdesa.model.entity.Organization;
import org.pamdesa.model.payload.request.CreateOrganizationRequest;
import org.pamdesa.model.payload.response.OrganizationResponse;
import org.pamdesa.model.payload.response.Response;
import org.pamdesa.service.OrganizationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController

@RequestMapping(AppPath.ORGANIZATION)
@RequiredArgsConstructor
public class OrganizationController {

  private final OrganizationService organizationService;

  @PostMapping
  public Response<Boolean> create(@Validated @RequestBody CreateOrganizationRequest request) {
    Organization savedOrganization = organizationService.save(request);
    return ResponseHelper.ok(Objects.nonNull(savedOrganization));
  }

  @GetMapping
  public Response<OrganizationResponse> findById(@RequestParam("id") String id) {
    return ResponseHelper.ok(organizationService.findById(id));
  }

}
