package com.onlineauctions.onlineauctions.pojo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageList<T> {
    private Long count;
    private List<T> data;

    public PageList(Page<T> page) {
        this.count = page.getTotal();
        this.data = page.getRecords();
    }
}
