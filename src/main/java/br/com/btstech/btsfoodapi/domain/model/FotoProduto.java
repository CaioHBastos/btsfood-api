package br.com.btstech.btsfoodapi.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
public class FotoProduto {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "produto_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Produto produto;

    private String nomeArquivo;
    private String descricao;
    private String contentType;
    private Long tamanho;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FotoProduto estado = (FotoProduto) o;
        return Objects.equals(id, estado.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public Long getRestauranteId() {
        if (getProduto() == null) {
            return null;
        }

        return getProduto().getRestaurante().getId();
    }
}
