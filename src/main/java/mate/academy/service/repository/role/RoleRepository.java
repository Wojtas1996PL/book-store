package mate.academy.service.repository.role;

import java.util.Optional;
import mate.academy.model.Role;
import mate.academy.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    @Query(value = "FROM Role WHERE name=:name")
    Optional<Role> getRoleByName(RoleName name);
}
