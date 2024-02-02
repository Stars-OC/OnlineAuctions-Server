package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.annotation.RequestPage;
import com.onlineauctions.onlineauctions.annotation.RequestToken;
import com.onlineauctions.onlineauctions.pojo.PageInfo;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.Result;
import com.onlineauctions.onlineauctions.pojo.auction.Cargo;
import com.onlineauctions.onlineauctions.pojo.type.Role;
import com.onlineauctions.onlineauctions.service.CargoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/cargo")
@RequiredArgsConstructor
@Validated
public class CargoController {

    private final CargoService cargoService;

    /**
     * 添加cargo
     *
     * @param cargo 要添加的cargo对象
     * @param username 添加cargo的卖家
     * @return 返回Result对象，其中包含添加结果
     */
    @PostMapping("/add")
    @Permission(Role.USER)
    public Result<Cargo> addCargo(@Validated @RequestBody Cargo cargo, @RequestToken("username") Long username){
        // 设置cargo的卖家为username
        cargo.setSeller(username);
        // 使用自增id
        cargo.setCargoId(null);
        // 调用cargoService的addCargo方法，添加cargo
        return cargoService.addCargo(cargo) ? Result.success("添加成功",cargo) : Result.failure();
    }

    /**
     * 更新货物信息
     *
     * @param cargo 货物对象
     * @param username 用户名
     * @return 更新成功返回更新后的货物信息，更新失败返回失败信息
     */
    @PostMapping("/update")
    @Permission(Role.USER)
    public Result<String> updateCargo(@Validated @RequestBody Cargo cargo, @RequestToken("username") Long username){
        // 设置卖家信息
        cargo.setSeller(username);
        // 调用cargoService的updateCargo方法，更新cargo
        Integer message = cargoService.updateCargo(cargo);
        // 返回更新结果
        return message != null ? Result.success("更新成功") : Result.failure("未知错误");
    }

    /**
     * 获取cargo信息
     *
     * @param cargoId cargo的ID
     * @return 返回Result对象，其中包含cargo信息
     */
    @GetMapping("/info/{cargoId}")
    public Result<Cargo> cargoInfo(@PathVariable Long cargoId){
        // 调用cargoService的cargoInfo方法，获取cargo信息
        Cargo cargo = cargoService.cargoInfo(cargoId);
        // 如果cargo不为空，返回Result.success(cargo)，否则返回Result.failure("查询 id " + cargoId + " 的货物失败")
        return cargo !=null ? Result.success(cargo) : Result.failure("查询 id " + cargoId + " 的货物失败");
    }

    /**
     * 获取 已发布的 拍卖列表
     *
     * @param pageInfo 分页信息
     * @return 返回Result对象，其中包含分页列表信息
     */
    @GetMapping("/published/list")
    public Result<PageList<Cargo>> cargoList(@RequestPage PageInfo pageInfo){
        // 调用cargoService的cargoList方法，获取cargo列表
        PageList<Cargo> pageList = cargoService.cargoList(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getFilter(),true);
        // 如果pageList不为空，返回Result.success(pageList)，否则返回Result.failure()
        return pageList != null? Result.success(pageList) : Result.failure();
    }


    @GetMapping("/audit/list")
    @Permission(Role.AUDIT_ADMIN)
    public Result<PageList<Cargo>> cargoAuditList(@RequestPage PageInfo pageInfo){
        // 调用cargoService的cargoList方法，获取cargo列表
        PageList<Cargo> pageList = cargoService.cargoList(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getFilter(),false);
        // 如果pageList不为空，返回Result.success(pageList)，否则返回Result.failure()
        return pageList != null? Result.success(pageList) : Result.failure();
    }

    @GetMapping("/audit/{cargo}")
    @Permission(Role.AUDIT_ADMIN)
    public Result<String> auditCargo(@PathVariable Long cargo,boolean audit){
        return cargoService.auditCargo(cargo,audit) ? Result.success("审核成功") : Result.failure("未知错误");
    }

    @PostMapping("/audit/update")
    @Permission(Role.AUDIT_ADMIN)
    public Result<Cargo> updateCargoStatus(@Validated @RequestBody Cargo cargo){
        return cargoService.updateCargoByAdmin(cargo) ? Result.success("修改成功") : Result.failure("未知错误");
    }
}
