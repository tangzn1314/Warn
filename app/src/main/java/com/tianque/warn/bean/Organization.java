package com.tianque.warn.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author ctrun on 2019/3/19
 * 组织机构实体类
 */
@SuppressWarnings("unused")
public class Organization implements Serializable {
	private static final long serialVersionUID = 5787216176425633284L;
	public static final long INVALID_ID = -1L;

	public long id;
	public String orgName;
	public String fullOrgName;
	public String contactWay;
	public String departmentNo;
	public String orgInternalCode = "";
	public OrgLevel orgLevel;


	public static class Community implements Serializable, Parcelable {
		private static final long serialVersionUID = 8381895894221140382L;

		public long orgId;
		public String orgName;

		public Community() {

		}

		protected Community(Parcel in) {
			orgId = in.readLong();
			orgName = in.readString();
		}

		public static final Creator<Community> CREATOR = new Creator<Community>() {
			@Override
			public Community createFromParcel(Parcel in) {
				return new Community(in);
			}

			@Override
			public Community[] newArray(int size) {
				return new Community[size];
			}
		};

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeLong(orgId);
			dest.writeString(orgName);
		}


		public static Community newEmpty() {
			Community community = new Community();
			community.orgId = INVALID_ID;
			community.orgName = "";
			return community;
		}
	}

	public static class OrgLevel implements Serializable {
		private static final long serialVersionUID = -2210176258251856707L;

		public static final int LEVEL_STREET = 20;
		public static final int LEVEL_COMMUNITY = 10;

		public int id;
		/**
		 * 10 社区、村
		 * 20 街道、镇
		 * 30 区
		 * 40 市
		 * 50 省
		 * 60 全国
		 */
		public int internalId;
		public String displayName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Organization that = (Organization) o;

		if (id != that.id) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

}
