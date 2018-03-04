package com.kartik.callprompt;

/**
 * Created by kartik on Sat, 24/2/18 in call-prompt.
 */

public class ContactInfo {
    private String contactName;
    private String contactNumber;
    private String contactPhotoURI;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactPhotoURI() {
        return contactPhotoURI;
    }

    public void setContactPhotoURI(String contactPhotoURI) {
        this.contactPhotoURI = contactPhotoURI;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", contactPhotoURI='" + contactPhotoURI + '\'' +
                '}';
    }

    @Override
    public boolean equals (Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            ContactInfo employee = (ContactInfo) object;
            if (this.contactNumber.equals(employee.getContactNumber())) {
                result = true;
            }
        }
        return result;
    }
}
