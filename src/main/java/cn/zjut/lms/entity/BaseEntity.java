package cn.zjut.lms.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {

//	@TableId(value = "id", type = IdType.AUTO)
//	private Long id;
//
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(value = "created_at",fill = FieldFill.INSERT)
	private LocalDateTime createdAt;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updatedAt;

}
