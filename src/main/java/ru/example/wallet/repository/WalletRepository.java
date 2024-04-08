package ru.example.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.example.wallet.model.Wallet;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    @Modifying
    @Query(nativeQuery = true,
            value = """
                    UPDATE wallet
                    SET amount = amount + :amount
                    WHERE id = :uuid                    
                    """)
    void addAmount(@Param("uuid") UUID uuid, @Param("amount") Double amount);
}
