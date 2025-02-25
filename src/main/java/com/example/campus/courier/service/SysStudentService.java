package com.example.campus.courier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.campus.courier.model.dto.DepartmentDTO;
import com.example.campus.courier.model.dto.StudentRecordDTO;
import com.example.campus.courier.model.entity.SysStudent;
import com.example.campus.courier.model.request.StudentAddRequest;
import com.example.campus.courier.model.request.StudentPageRequest;
import com.example.campus.courier.model.request.StudentProfileUpdateRequest;
import com.example.campus.courier.model.request.StudentUpdateRequest;
import com.example.campus.courier.model.response.ImportResult;
import com.example.campus.courier.model.response.PageResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "学生信息服务")
public interface SysStudentService extends IService<SysStudent> {

    /**
     * 获取院系专业年级信息
     *
     * @return 院系信息列表
     */
    List<DepartmentDTO> getDepartments();

    /**
     * 新增学生
     *
     * @param request 学生信息
     */
    void addStudent(StudentAddRequest request);

    /**
     * 分页查询学生列表
     *
     * @param request 查询参数
     * @return 分页结果
     */
    PageResult<SysStudent> page(StudentPageRequest request);

    /**
     * 下载学生导入模板
     */
    void downloadTemplate(HttpServletResponse response) throws IOException;

    /**
     * 导入学生信息
     *
     * @param file Excel文件
     * @return 导入结果
     */
    ImportResult importStudents(MultipartFile file) throws IOException;

    /**
     * 更新学生信息
     *
     * @param request 更新请求
     */
    void update(StudentUpdateRequest request);

    /**
     * 重置学生密码
     * 重置后的密码为：学号 + 姓名首字母
     *
     * @param id 学生ID
     */
    void resetPassword(Integer id);

    /**
     * 删除学生
     *
     * @param id 学生ID
     */
    void delete(Integer id);

    /**
     * 获取学生记录列表
     *
     * @param studentId 学生ID
     * @return 记录列表
     */
    List<StudentRecordDTO> getStudentRecords(Long studentId);

    SysStudent getByStudentNo(String studentNo);

    /**
     * 学生登录
     *
     * @param studentNo 学号
     * @param password  密码
     * @return token值
     */
    String login(String studentNo, String password);

    /**
     * 学生退出登录
     */
    void logout();

    /**
     * 获取当前登录学生信息
     *
     * @param studentId 学生ID
     * @return 学生信息
     */
    SysStudent getCurrentStudent(Integer studentId);

    /**
     * 更新当前学生个人信息
     *
     * @param studentId 学生ID
     * @param request   更新请求
     */
    void updateProfile(Integer studentId, StudentProfileUpdateRequest request);
}