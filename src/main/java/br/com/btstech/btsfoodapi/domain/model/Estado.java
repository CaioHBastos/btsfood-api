package br.com.btstech.btsfoodapi.domain.model;

import br.com.btstech.btsfoodapi.core.validation.Groups;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Estado {

    @NotNull(groups = Groups.EstadoId.class)
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Estado estado = (Estado) o;
        return Objects.equals(id, estado.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
