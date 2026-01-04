package backend.DataLayer.protocol.Role;

public interface RoleStructure
{
    Integer getPersonId();
    String getPersonName();
    RoleEntity.Role getRole();
    void setPersonId(int personId);
    void setPersonName(String personName);
    void setRole(RoleEntity.Role role);

    // Role enum

}


