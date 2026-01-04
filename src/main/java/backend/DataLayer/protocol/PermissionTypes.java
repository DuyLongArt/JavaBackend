package backend.DataLayer.protocol;

public enum PermissionTypes implements TypeGetSet
{

    READ{
        @Override
        public String get()
        {
            return "READ";
        }



        @Override
        public void set(String read)
        {

        }



        @Override
        public String toString() {
            return "READ";
        }
    },
    WRITE{
        @Override
        public String get()
        {
            return "WRITE";
        }


        @Override
        public void set(String write)
        {

        }

        @Override
        public String toString() {
            return "WRITE";
        }
    }
}
