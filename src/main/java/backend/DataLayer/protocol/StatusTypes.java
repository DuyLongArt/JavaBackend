package backend.DataLayer.protocol;

public enum StatusTypes implements TypeGetSet
{
    ACTIVE{
        @Override
        public String get()
        {
            return "ACTIVE";
        }

        @Override
        public void set(String value)
        {

        }

        @Override
        public String toString() {
            return "ACTIVE";
        }
    }
    ,
    INACTIVE{
        @Override
        public String get()
        {
            return "INACTIVE";
        }

        @Override
        public void set(String value)
        {

        }

        @Override
        public String toString() {
            return "INACTIVE";
        }
    }
    ,
    PENDING{
        @Override
        public String get()
        {
            return "PENDING";
        }

        @Override
        public void set(String value)
        {

        }

        @Override
        public String toString() {
            return "PENDING";
        }
    }

    ,

    DELETED{
        @Override
        public String get()
        {
            return "DELETED";
        }

        @Override
        public void set(String value)
        {

        }

        @Override
        public String toString() {
            return "DELETED";
        }
    };



}
