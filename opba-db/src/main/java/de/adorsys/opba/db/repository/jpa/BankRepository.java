package de.adorsys.opba.db.repository.jpa;

import de.adorsys.opba.db.domain.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    Optional<Bank> findByUuid(UUID uuid);
}
