package br.edu.unidavi.api.view;


import br.edu.unidavi.api.domain.Cliente;
import br.edu.unidavi.api.domain.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static br.edu.unidavi.api.domain.Cliente.nomeContem;
import static br.edu.unidavi.api.domain.Cliente.produtoIdIgual;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.jpa.domain.Specifications.where;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@ExposesResourceFor(Cliente.class)
@RequestMapping(value = "/clientes", produces = HAL_JSON_VALUE)
public class ClienteController extends AbstractController<Long, Cliente, ClienteRepository> {

    @Autowired
    public ClienteController(ClienteRepository repository, ConversionService conversionService, PagedResourcesAssembler<Cliente> pagedResourcesAssembler, ClienteResourceAssembler assembler){
        super(repository, conversionService, pagedResourcesAssembler, assembler);
    }

    @RequestMapping(method = GET, produces = HAL_JSON_VALUE)
    @Override
    public ResponseEntity<PagedResources<Resource<Cliente>>> findAll(org.springframework.data.domain.Pageable pageable){
        return super.findAll(pageable);
    }

    @RequestMapping(method = GET, value = "/byNomeContendo/{nome:.+}")
    public ResponseEntity<Resources<Resource<Cliente>>> findByNomeContendo(@PathVariable String nome) {
        return ok(new Resources<>(repository.findAll(where(nomeContem(nome))).stream()
                .map(assembler::toResource)
                .collect(toList())));
    }

    @RequestMapping(method = GET, value = "/byProdutoId/{produtoId}")
    public ResponseEntity<Resources<Resource<Cliente>>> findByProdutoId(@PathVariable Long produtoId) {
        return ok(new Resources<>(repository.findAll(where(produtoIdIgual(produtoId))).stream()
                .map(assembler::toResource)
                .collect(toList())));
    }

    @Component
    private static class ClienteResourceAssembler implements ResourceAssembler<Cliente, Resource<Cliente>> {

        @Autowired
        private EntityLinks links;

        @Override
        public Resource<Cliente> toResource(Cliente entity) {
            return new Resource<>(entity,
                    links.linkFor(Cliente.class, entity.getId()).withSelfRel(),
                    linkTo(methodOn(ProdutoController.class).findByClienteId(entity.getId())).withRel("produto"));
        }
    }
}
