package com.jb.jbank.role.services;

import com.jb.jbank.exceptions.specificExceptions.BadRequestException;
import com.jb.jbank.exceptions.specificExceptions.NotFoundException;
import com.jb.jbank.res.Response;
import com.jb.jbank.role.entity.Role;
import com.jb.jbank.role.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Response<Role> createRole(Role roleRequest) {

        if (roleRepository.findByName(roleRequest.getName()).isPresent()) {
            throw new BadRequestException("Role already exists");
        }

        Role savedRole = roleRepository.save(roleRequest);

        return Response.<Role>builder().statusCode(HttpStatus.OK.value()).message("Role saved successfully").data(savedRole).build();
    }

    @Override
    public Response<Role> updateRole(Role roleRequest) {

        Role role = roleRepository.findById(roleRequest.getId()).orElseThrow(() -> new NotFoundException("Role not found"));

        role.setName(roleRequest.getName());
        Role updatedRole = roleRepository.saveAndFlush(role);
        return Response.<Role>builder().statusCode(HttpStatus.OK.value()).message("Role saved successfully").data(updatedRole).build();
    }

    @Override
    public Response<List<Role>> getAllRoles() {

        List<Role> roles = roleRepository.findAll();

        String message = roles.size() >= 1 ? "Roles" : "No roles found";

        return Response.
                <List<Role>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .data(roles)
                .build();

    }

    @Override
    public Response<?> deleteRole(Long id) {

        if (!roleRepository.existsById(id)) {
            throw new NotFoundException("Role not found");
        }

        roleRepository.deleteById(id);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role deleted successfully")
                .build();

    }
}
