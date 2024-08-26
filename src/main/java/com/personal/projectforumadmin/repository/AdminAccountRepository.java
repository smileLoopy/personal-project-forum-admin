package com.personal.projectforumadmin.repository;

import com.personal.projectforumadmin.domain.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminAccountRepository extends JpaRepository<AdminAccount, String> {
}
