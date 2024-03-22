package com.batsworks.batch.domain.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class XmlEvento implements Serializable {

    String infEvento;

}
