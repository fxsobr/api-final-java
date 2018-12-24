package br.edu.unidavi.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;
import javax.persistence.criteria.Join;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

@Entity
@Value(staticConstructor = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Relation(value = "produto", collectionRelation = "produtos")
public class Produto implements Serializable, Identifiable<Long> {

    private static final byte serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonFinal private Long id;

    @NotNull(message = "O campo nome é obrigatório!")
    @Size(min = 2, max = 100, message = "O campo nome deve ter entre {min} e {max} caracteres!")
    @Column(nullable = false, length = 100)
    @NonFinal private String nome;

    @NotNull(message = "O campo descrição é obrigatório!")
    @Size(min = 2, max = 300, message = "O campo descrição deve ter entre {min} e {max} caracteres!")
    @Column(nullable = false, length = 300)
    @NonFinal private String descricao;

    @NotNull(message = "O campo marca é obrigatório!")
    @Size(min = 2, max = 30, message = "O campo marca deve ter entre {min} e {max} caracteres!")
    @Column(nullable = false, length = 30)
    @NonFinal private String marca;

    @NotNull(message = "O campo valor é obrigatório!")
    @Size(min = 1, max = 10, message = "O campo nome deve ter entre {min} e {max} caracteres!")
    @Column(nullable = false, length = 10)
    @NonFinal private Double valor;

    @JsonIgnore
    @OneToMany(mappedBy = "produto")
    @NonFinal private List<Cliente> clientes = newLinkedList();

    public static Specification<Produto> nomeContem(String nome) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Produto_.nome).as(String.class)), '%'+nome.toLowerCase()+'%');
    }

    public static Specification<Produto> marcaIgual(String marca) {
        return (root, query, cb) -> cb.equal(root.get(Produto_.marca), marca);
    }

    public static Specification<Produto> clienteIdIgual(Long clienteId) {
        return (root, query, cb) -> {
            Join<Produto, Cliente> clientesJoin = root.join(Produto_.clientes);
            return cb.equal(clientesJoin.get(Cliente_.id), clienteId);
        };
    }

}
