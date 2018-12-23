package br.edu.unidavi.api.view;


import br.edu.unidavi.api.domain.Produto;
import br.edu.unidavi.api.domain.ProdutoRepository;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ExposesResourceFor(Produto.class)
@RequestMapping(value = "/produtos")
public class ProdutoController extends AbstractController<Long, Produto, ProdutoRepository> {
}
