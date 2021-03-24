package org.tat.gginl.api.domains.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.tat.gginl.api.domains.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {

	@Query("SELECT a.accountCode FROM CoaSetup a WHERE a.acccountName = :acccountName AND a.currency = :currency AND a.branchCode = :branchCode")
	public String findCheckOfAccountNameByCode(@Param("acccountName") String accountName, @Param("branchCode") String branchCode, @Param("currency") String currency);

	@Query("SELECT p FROM Payment p WHERE p.referenceNo = :referenceNo")
	public Payment findByPaymentReferenceNo(@Param("referenceNo") String referenceNo);

	@Query("SELECT p FROM Payment p WHERE p.referenceNo = :referenceNo and p.complete ='false' ")
	public Optional<Payment> findPaymentNotComplete(@Param("referenceNo") String referenceNo);

	@Transactional
	@Query(nativeQuery = true, value = "SELECT ccoa.id FROM CCOA ccoa JOIN Coa coa ON coa.id = ccoa.coaId WHERE coa.acCode = ?1 AND ccoa.branchId = ?2 AND ccoa.currencyId = ?3 ")
	public String findCCOAByCode(String acCode, String branchId, String currencyId);

}
