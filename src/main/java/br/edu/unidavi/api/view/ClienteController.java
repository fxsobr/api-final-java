package br.edu.unidavi.api.view;


import br.edu.unidavi.api.domain.Cliente;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

@RestController
@ExposesResourceFor(Cliente.class)
@RequestMapping(value = "/clientes", produces = HAL_JSON_VALUE)
public class ClienteController  {
}
