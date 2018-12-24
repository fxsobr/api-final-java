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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Entity
@Value(staticConstructor = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Relation(value = "cliente", collectionRelation = "clientes")
public class Cliente implements Serializable, Identifiable<Long> {

    private static final byte serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonFinal private Long id;

    @NotNull(message = "O campo nome é obrigatório!!!")
    @Size(min = 1,max = 100, message = "O campo nome deve ter entre {min} e {max} caracteres")
    @Column(nullable = false, length = 100)
    @NonFinal private String nome;

    @NotNull(message = "O campo email é obrigatório!!!")
    @Column(nullable = false, length = 30)
    @NonFinal private String email;

    @NotNull(message = "O campo cpf é obrigatório!!!")
    @Column(nullable = false, length = 11)
    @NonFinal private String cpf;

    @NotNull(message = "O campo email é obrigatório!!!")
    @Column(nullable = false, length = 30)
    @NonFinal private String rua;

    @NotNull(message = "O campo cidade é obrigatório!!!")
    @Column(nullable = false, length = 30)
    @NonFinal private String cidade;

    @NotNull(message = "O campo estado é obrigatório!!!")
    @Size(min = 2,max = 2, message = "O campo estado deve ter entre {min} e {max} caracteres")
    @Column(nullable = false, length = 2)
    @NonFinal private String estado;

    @NotNull(message = "O campo cep é obrigatório!!!")
    @Column(nullable = false, length = 8)
    @NonFinal private int cep;


    @JsonIgnore
    @NotNull(message = "O campo produto é obrigatório!")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NonFinal private Produto produto;


    public static Specification<Cliente> nomeContem(String nome) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Cliente_.nome).as(String.class)), '%'+nome+'%');
    }

    public static Specification<Cliente> ruaContem(String rua) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Cliente_.rua).as(String.class)), '%'+rua+'%');
    }

    public static Specification<Cliente> cidadeContem(String cidade) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Cliente_.cidade).as(String.class)), '%'+cidade+'%');
    }

    public static Specification<Cliente> estadoContem(String estado) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Cliente_.estado).as(String.class)), '%'+estado+'%');
    }

    public static Specification<Cliente> produtoIdIgual(Long produtoId) {
        return (root, query, cb) -> cb.equal(root.get(Cliente_.produto).get(Produto_.id), produtoId);
    }
}
