package de.adorsys.opba.db.repository.jpa.psu;

import de.adorsys.opba.db.domain.entity.Bank;
import de.adorsys.opba.db.domain.entity.psu.PsuAspspPrvKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PsuAspspPrvKeyRepository extends CrudRepository<PsuAspspPrvKey, UUID> {

    Optional<PsuAspspPrvKey> findByPsuIdAndAspspId(long psuId, long aspspId);
    void deleteByAspsp(Bank bank);
}
