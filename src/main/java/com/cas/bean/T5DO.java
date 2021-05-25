package com.cas.bean;

import com.cas.mybatis.base.BaseModel;
import java.io.Serializable;

public class T5DO extends BaseModel implements Serializable {
    private String c1;

    private String c2;

    private String c3;

    private String c4;

    private String c5;

    private static final long serialVersionUID = 1L;

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1 == null ? null : c1.trim();
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2 == null ? null : c2.trim();
    }

    public String getC3() {
        return c3;
    }

    public void setC3(String c3) {
        this.c3 = c3 == null ? null : c3.trim();
    }

    public String getC4() {
        return c4;
    }

    public void setC4(String c4) {
        this.c4 = c4 == null ? null : c4.trim();
    }

    public String getC5() {
        return c5;
    }

    public void setC5(String c5) {
        this.c5 = c5 == null ? null : c5.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", c1=").append(c1);
        sb.append(", c2=").append(c2);
        sb.append(", c3=").append(c3);
        sb.append(", c4=").append(c4);
        sb.append(", c5=").append(c5);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        T5DO other = (T5DO) that;
        return (this.getC1() == null ? other.getC1() == null : this.getC1().equals(other.getC1()))
            && (this.getC2() == null ? other.getC2() == null : this.getC2().equals(other.getC2()))
            && (this.getC3() == null ? other.getC3() == null : this.getC3().equals(other.getC3()))
            && (this.getC4() == null ? other.getC4() == null : this.getC4().equals(other.getC4()))
            && (this.getC5() == null ? other.getC5() == null : this.getC5().equals(other.getC5()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getC1() == null) ? 0 : getC1().hashCode());
        result = prime * result + ((getC2() == null) ? 0 : getC2().hashCode());
        result = prime * result + ((getC3() == null) ? 0 : getC3().hashCode());
        result = prime * result + ((getC4() == null) ? 0 : getC4().hashCode());
        result = prime * result + ((getC5() == null) ? 0 : getC5().hashCode());
        return result;
    }
}