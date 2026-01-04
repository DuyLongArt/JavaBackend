package backend.DataLayer.protocol;

public enum RoleTypes implements TypeGetSet
{
    ADMIN{
        @Override
        public String get()
        {
            return "ADMIN";
        }

        @Override
        public void set(String value)
        {

        }

        @Override
        public String toString() {
            return "ADMIN";
        }
    },
    USER{
        @Override
        public String get()
        {
            return "USER";
        }

        @Override
        public void set(String value)
        {

        }

        @Override
        public String toString() {
            return "USER";
        }
    },
    VIEWER{
        @Override
        public String get()
        {
            return "VIEWER";
        }

        @Override
        public void set(String value)
        {

        }

        @Override
        public String toString(){
            return "VIEWER";
        }
    }
}
