package org.pamdesa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pamdesa.model.entity.Organization;
import org.pamdesa.model.enums.ErrorCode;
import org.pamdesa.model.error.ClientException;
import org.pamdesa.model.payload.request.CreateOrganizationRequest;
import org.pamdesa.model.payload.response.OrganizationResponse;
import org.pamdesa.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationService {

  private final OrganizationRepository organizationRepository;

  public Organization save(CreateOrganizationRequest request) {
    if (organizationRepository.existsByName(request.getName())) {
      log.info("organization with name {} already exists", request.getName());
      throw new ClientException(ErrorCode.DATA_ALREADY_EXIST);
    }
    return organizationRepository.save(Organization.builder()
            .address(request.getAddress())
            .description(request.getDescription())
            .name(request.getName())
            .logo(request.getLogo())
        .build());
  }

  public OrganizationResponse findById(String id) {
    return organizationRepository.findById(id)
        .map(organization -> OrganizationResponse.builder()
            .id(organization.getId())
            .description(organization.getDescription())
            .logo(organization.getLogo())
            .name(organization.getName())
            .build())
        .orElseThrow(()-> new ClientException(ErrorCode.DATA_NOT_FOUND));

  }

}
