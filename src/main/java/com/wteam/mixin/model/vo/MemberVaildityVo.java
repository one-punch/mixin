package com.wteam.mixin.model.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.define.IValueObject;

public class MemberVaildityVo implements IValueObject{
    

    private Long id;
	@NotNull
	private Long memberId;
	@NotNull
	private BigDecimal cost;
	@NotNull
	private int memberVaildity;

	public MemberVaildityVo() {	}

	public MemberVaildityVo(Long memberId, BigDecimal cost, int memberVaildity) {
		this.memberId = memberId;
		this.cost = cost;
		this.memberVaildity = memberVaildity;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public int getMemberVaildity() {
		return memberVaildity;
	}

	public void setMemberVaildity(int memberVaildity) {
		this.memberVaildity = memberVaildity;
	}

	@Override
	public String toString() {
		return "MemberVaildityVo [memberId=" + memberId + ", cost=" + cost + ", memberVaildity=" + memberVaildity + "]";
	}
}
