package com.victormacedov.library_api.controller;

import com.victormacedov.library_api.model.Client;
import com.victormacedov.library_api.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Endpoints para cadastro de clients")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Salvar Clients", description = "Salva um novo client.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client salvo com sucesso."),
    })
    public void salvar(@RequestBody Client client){
        clientService.salvar(client);
    }
}
