package backend.DataLayer.protocol.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public  class RoleEntity implements RoleStructure
{
    @Id
    @Column(name="role_id", nullable = false)
    private int roleId;
    private int personId;
    private String personName;
    private Role role;
    public enum Role
    {
        USER,
        ADMIN,
        VIEWER
    }
        public RoleEntity(int personId, String personName, Role role) {
        this.personId = personId;
        this.personName = personName;
        this.role = role;
    }

    @Override
    @Column(name="person_id", nullable = false)
    public Integer getPersonId() {
        return personId;
    }

    @Override
    public String getPersonName() {
        return personName;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public void setPersonId(int personId)
    {

    }

    @Override
    public void setPersonName(String personName)
    {

    }

    @Override
    public void setRole(Role role)
    {

    }
}
