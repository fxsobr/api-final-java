package br.edu.unidavi.api.view;


import br.edu.unidavi.api.domain.EntityNotFoundException;
import br.edu.unidavi.api.domain.Produto;
import br.edu.unidavi.api.domain.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.edu.unidavi.api.domain.Produto.clienteIdIgual;
import static br.edu.unidavi.api.domain.Produto.marcaIgual;
import static br.edu.unidavi.api.domain.Produto.nomeContem;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.jpa.domain.Specifications.where;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@ExposesResourceFor(Produto.class)
@RequestMapping(value = "/produtos")
public class ProdutoController extends AbstractController<Long, Produto, ProdutoRepository> {

    @Autowired
    public ProdutoController(ProdutoRepository repository, ConversionService conversionService, PagedResourcesAssembler<Produto> pagedResourcesAssembler, ProdutoResourceAssembler assembler) {
        super(repository, conversionService, pagedResourcesAssembler,  assembler);
    }
    @RequestMapping(method = GET, produces = HAL_JSON_VALUE)
    @Override
    public ResponseEntity<PagedResources<Resource<Produto>>> findAll(Pageable pageable) {
        return super.findAll(pageable);
    }

    @RequestMapping(method = GET, value = "/byMarca/{marca:\\w{2}}", produces = HAL_JSON_VALUE)
    public ResponseEntity<Resource<Produto>> findByMarca(@PathVariable String marca) {
        Produto produto = repository.findOne(where(marcaIgual(marca)));
        if (nonNull(produto)) return ok(assembler.toResource(produto));
        throw new EntityNotFoundException(String.format("Marca %s não encontrada!", marca));
    }

    @RequestMapping(method = GET, value = "/byNomeContendo/{nome:.+}", produces = HAL_JSON_VALUE)
    public ResponseEntity<Resources<Resource<Produto>>> findByNomeContendo(@PathVariable String nome) {
        return ok(new Resources<>(repository.findAll(where(nomeContem(nome))).stream()
                .map(assembler::toResource)
                .collect(toList())));
    }

    @RequestMapping(method = GET, value = "/byClienteId/{clienteId}", produces = HAL_JSON_VALUE)
    public ResponseEntity<Resource<Produto>> findByClienteId(@PathVariable Long clienteId) {
        Produto produto = repository.findOne(where(clienteIdIgual(clienteId)));
        if (nonNull(produto)) return ok(assembler.toResource(produto));
        throw new EntityNotFoundException(String.format("Produto com cliente %s não encontrado!", clienteId));
    }

    @Component
    private static class ProdutoResourceAssembler implements ResourceAssembler<Produto, Resource<Produto>> {

        @Autowired
        private EntityLinks links;

        @Override
        public Resource<Produto> toResource(Produto entity) {
            return new Resource<>(entity,
                    links.linkFor(Produto.class, entity.getId()).withSelfRel(),
                    linkTo(methodOn(ClienteController.class).findByProdutoId(entity.getId())).withRel("clientes"));
        }
    }
}
