package br.com.btstech.btsfoodapi.domain.repository;

import br.com.btstech.btsfoodapi.domain.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

}
