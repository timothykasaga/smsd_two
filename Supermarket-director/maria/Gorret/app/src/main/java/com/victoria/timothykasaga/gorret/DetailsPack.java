package com.victoria.timothykasaga.gorret;

/**
 * Created by Leontymo on 4/23/2016.
 */

import android.os.Parcel;
        import android.os.Parcel;
        import android.os.Parcelable;
        import android.os.Parcelable.Creator;

public class DetailsPack
        implements Parcelable
{
    public static final Creator CREATOR = new Creator()
    {
        public DetailsPack createFromParcel(Parcel paramAnonymousParcel)
        {
            return new DetailsPack(paramAnonymousParcel);
        }

        public DetailsPack[] newArray(int paramAnonymousInt)
        {
            return new DetailsPack[paramAnonymousInt];
        }
    };
    String admin;
    String d_lat;
    String d_log;
    String s_desc;
    String s_email;
    float[] s_latlng = new float[2];
    String s_location;
    String s_name;
    String s_phone;
    String s_website;

    protected DetailsPack(Parcel paramParcel)
    {
        this.s_name = paramParcel.readString();
        this.s_location = paramParcel.readString();
        this.s_website = paramParcel.readString();
        this.s_email = paramParcel.readString();
        this.s_phone = paramParcel.readString();
        this.s_desc = paramParcel.readString();
        this.d_lat = paramParcel.readString();
        this.d_log = paramParcel.readString();
        this.admin = paramParcel.readString();
        this.s_latlng = paramParcel.createFloatArray();
    }

    public DetailsPack(String s_name, String s_location, String s_website, String s_email, String s_phone,
                       String s_desc, String d_lat, String d_log, String admin)
    {
        this.s_name = s_name;
        this.s_location = s_location;
        this.s_website = s_website;
        this.s_email = s_email;
        this.s_phone = s_phone;
        this.s_desc = s_desc;
        this.d_lat = d_lat;
        this.d_log = d_log;
        this.admin = admin;
    }

    public int describeContents()
    {
        return 0;
    }

    public String getAdmin()
    {
        return this.admin;
    }

    public String getD_lat()
    {
        return this.d_lat;
    }

    public String getD_log()
    {
        return this.d_log;
    }

    public String getS_desc()
    {
        return this.s_desc;
    }

    public String getS_email()
    {
        return this.s_email;
    }

    public float[] getS_latlng()
    {
        return this.s_latlng;
    }

    public String getS_location()
    {
        return this.s_location;
    }

    public String getS_name()
    {
        return this.s_name;
    }

    public String getS_phone()
    {
        return this.s_phone;
    }

    public String getS_website()
    {
        return this.s_website;
    }

    public void setAdmin(String paramString)
    {
        this.admin = paramString;
    }

    public void setD_lat(String paramString)
    {

        this.d_lat = paramString;
    }

    public void setD_log(String paramString)
    {
        this.d_log = paramString;
    }

    public void setS_desc(String paramString)
    {
        this.s_desc = paramString;
    }

    public void setS_email(String paramString)
    {
        this.s_email = paramString;
    }

    public void setS_latlng(float[] paramArrayOfFloat)
    {
        this.s_latlng = paramArrayOfFloat;
    }

    public void setS_location(String paramString)
    {
        this.s_location = paramString;
    }

    public void setS_name(String paramString)
    {
        this.s_name = paramString;
    }

    public void setS_phone(String paramString)
    {
        this.s_phone = paramString;
    }

    public void setS_website(String paramString)
    {
        this.s_website = paramString;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
        paramParcel.writeString(this.s_name);
        paramParcel.writeString(this.s_location);
        paramParcel.writeString(this.s_website);
        paramParcel.writeString(this.s_email);
        paramParcel.writeString(this.s_phone);
        paramParcel.writeString(this.s_desc);
        paramParcel.writeString(this.d_lat);
        paramParcel.writeString(this.d_log);
        paramParcel.writeString(this.admin);
        paramParcel.writeFloatArray(this.s_latlng);
    }
}
