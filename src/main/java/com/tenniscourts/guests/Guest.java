package com.tenniscourts.guests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tenniscourts.config.persistence.BaseEntity;
import com.tenniscourts.reservations.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class Guest extends BaseEntity<Long> {

  @Column
  @NotNull
  private String name;

  @OneToOne(fetch = FetchType.LAZY,
      cascade =  CascadeType.ALL,
      mappedBy = "guest")
  @JsonIgnore
  private Reservation reservation;
}
