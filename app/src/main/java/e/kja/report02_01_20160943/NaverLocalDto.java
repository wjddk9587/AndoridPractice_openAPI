package e.kja.report02_01_20160943;

import java.io.Serializable;

public class NaverLocalDto implements Serializable {

        private int _id;
        private String title;
        private String address;
        private String telephone;
        private String category;

        public int get_id() { return _id; }

        public void set_id(int _id) { this._id = _id; }

        public String getTitle() { return title; }

        public void setTitle(String title) { this.title = title; }

        public String getAddress() { return address; }

        public void setAddress(String address) { this.address = address; }

        public void setCategory(String category){this.category = category;}

        public String getCategory() { return category; }

        public String getTelephone() { return telephone; }

        public void setTelephone(String telephone) { this.telephone = telephone; }
}
