package com.onlineauctions.onlineauctions.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlineauctions.onlineauctions.mapper.CargoMapper;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.auction.Cargo;
import com.onlineauctions.onlineauctions.pojo.type.CargoStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CargoService {
    private final CargoMapper cargoMapper;

    public boolean addCargo(Cargo cargo) {
        return cargoMapper.insert(cargo) > 0;
    }

    /**
     * 查询拍卖物品的信息
     *
     * @param cargoId 要查询的cargo的ID
     * @return 查询到的cargo对象
     */
    public Cargo cargoInfo(Long cargoId) {
        return cargoMapper.selectById(cargoId);
    }

    /**
     * 获取 拍卖物品 的分页列表
     *
     * @param pageNum 页码
     * @param pageSize 每页显示数量
     * @param filter 过滤条件
     * @return 分页列表
     */
    public PageList<Cargo> cargoListLimit(int pageNum, int pageSize, String filter, boolean published) {
        // 创建查询条件
        QueryWrapper<Cargo> queryWrapper = new QueryWrapper<>();
        // 过滤条件
        // 查询已发布 和 拍卖的
        if (published) {
            queryWrapper.between("status", CargoStatus.PUBLISHED.getStatus(), CargoStatus.SELLING.getStatus());
        }
        else {
            queryWrapper.eq("status", CargoStatus.AUDIT.getStatus());

        }
        return cargoList(pageNum, pageSize, filter, queryWrapper);
    }

    /**
     * 更新货物信息
     *
     * @param cargo 货物对象
     * @return 更新成功返回更新后的货物状态，更新失败或未找到货物返回-1
     */
    public Integer updateCargo(Cargo cargo) {
        // 创建QueryWrapper对象
        QueryWrapper<Cargo> queryWrapper = new QueryWrapper<>();
        // 验证cargo，将验证结果设置到queryWrapper中
        verifyCargo(cargo, queryWrapper);

        // 调用cargoMapper的update方法，更新cargo
        // 如果更新成功，返回true，否则返回false
        boolean update = cargoMapper.update(cargo, queryWrapper) > 0;
        if (update) return CargoStatus.AUDIT.getStatus();

        // 如果更新失败，查询货物信息
        Cargo select = cargoMapper.selectById(cargo.getCargoId());
        if (select == null) return null;
        return select.getStatus();
    }

    /**
     * 验证货物信息
     *
     * @param cargo 货物对象
     * @param queryWrapper 查询条件封装对象
     */
    public void verifyCargo(Cargo cargo, QueryWrapper<Cargo> queryWrapper) {
        queryWrapper.eq("cargo_id", cargo.getCargoId()) // 根据cargo_id等于指定值进行查询
                .lt("status", CargoStatus.SOLD.getStatus()) // 根据status不是SOLD以上的状态进行查询
                .eq("seller", cargo.getSeller()); // 根据seller等于指定值进行查询
    }

    /**
     * 更新 拍卖物品 的审核状态
     *
     * @param cargoId cargo的ID
     * @param audit    是否进行审计
     * @return 如果更新成功，返回true；否则返回false
     */
    public boolean auditCargo(Long cargoId, boolean audit) {
        CargoStatus status = audit ? CargoStatus.PUBLISHED : CargoStatus.FAILED_AUDIT;
        return cargoMapper.updateCargoStatus(cargoId, status.getStatus()) > 0;
    }

    /**
     * 根据管理员更新cargo
     *
     * @param cargo 要更新的cargo对象
     * @return 如果更新成功，返回true；否则返回false
     */
    public boolean updateCargoByAdmin(Cargo cargo) {
        QueryWrapper<Cargo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cargo_id", cargo.getCargoId()) // 根据cargo_id等于指定值进行查询
                .lt("status", CargoStatus.SOLD.getStatus()); // 根据status不是SOLD以上的状态进行查询
        return cargoMapper.update(cargo,queryWrapper) > 0;
    }

    /**
     * 分页查询Cargo对象。
     *
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param filter  查询条件
     * @param queryWrapper 查询条件包装器
     * @return 分页列表
     */
    public PageList<Cargo> cargoList(int pageNum, int pageSize, String filter, QueryWrapper<Cargo> queryWrapper) {
        // 如果queryWrapper为空，则创建一个新的QueryWrapper对象
        if (queryWrapper == null) queryWrapper = new QueryWrapper<>();
        // 如果filter不为空，则在"name"列上应用LIKE查询
        if (!StringUtils.isEmpty(filter)) queryWrapper.like("name", filter);
        // 按照"create_at"列的升序排序
        queryWrapper.orderByAsc("create_at");
        // 创建分页对象
        Page<Cargo> userPage = new Page<>(pageNum, pageSize);
        // 执行查询并获取分页结果
        Page<Cargo> selectPage = cargoMapper.selectPage(userPage, queryWrapper);
        // 返回分页列表
        return new PageList<>(selectPage);
    }

    /**
     * 获取用户拥有的货物列表
     *
     * @param pageNum 页码
     * @param pageSize 每页显示数量
     * @param filter 过滤条件
     * @param username 用户名
     * @return 货物列表
     */
    public PageList<Cargo> cargoUserList(int pageNum, int pageSize, String filter, long username) {
        // 创建查询条件
        QueryWrapper<Cargo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seller", username);

        // 调用cargoList方法获取货物列表
        return cargoList(pageNum, pageSize, filter, queryWrapper);
    }

}
