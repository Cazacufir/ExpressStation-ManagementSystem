package com.parcelhub.vo;

import com.parcelhub.entity.Parcel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveParcelVo {

    private String route;
    private String hubName;
    private List<Parcel> parcel;
}
