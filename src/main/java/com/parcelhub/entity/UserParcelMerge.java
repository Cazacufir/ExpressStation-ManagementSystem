package com.parcelhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_parcel_merge")
public class UserParcelMerge implements Serializable {
    @TableId(type = IdType.AUTO)
    private int mapId;

    private int user_id;

    private int parcel_id;

}
