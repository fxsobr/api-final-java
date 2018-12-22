package br.edu.unidavi.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Entity
@Value(staticConstructor = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Relation(value = "cliente", collectionRelation = "clientes")
public class Cliente implements Serializable, Identifiable<Long> {

    private static final byte serialVersionUID = 1;

    @Id
    @GeneratedValue
    @NonFinal private Long id;

    @NotNull(message = "O campo nome é obrigatório!!!")
    @Size(min = 1,max = 100, message = "O campo nome deve ter entre {min} e {max} caracteres")
    @Column(nullable = false, length = 100)
    @NonFinal private String nome;

    @JsonIgnore
    @NotNull(message = "O campo produto é obrigatório!")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NonFinal private Produto produto;

}
