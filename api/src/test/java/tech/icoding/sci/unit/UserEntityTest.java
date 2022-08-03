package tech.icoding.sci.unit;

import com.alibaba.fastjson.JSON;
import org.jeasy.random.EasyRandom;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import tech.icoding.sci.core.repository.UserRepository;
import tech.icoding.sci.sdk.common.PageData;
import tech.icoding.sci.sdk.data.UserData;
import tech.icoding.sci.sdk.form.admin.UserForm;


/**
 * @author : Joe
 * @date : 2022/5/9
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserEntityTest extends AbstractMvcTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void curd() throws Exception
    {
        EasyRandom easyRandom = new EasyRandom();

        // 新增
        UserForm userForm = easyRandom.nextObject(UserForm.class);
        UserData userData = create("/users",UserData.class, userForm);
        Assert.assertEquals(userForm.getNickName(), userData.getNickName());

        // 修改
        userData  = get("/users/{id}",UserData.class, userData.getId());
        Assert.assertEquals(userForm.getNickName(), userData.getNickName());

        // 详情
        userForm = easyRandom.nextObject(UserForm.class);
        userData = update("/users/{id}",UserData.class, JSON.toJSONString(userForm),userData.getId());
        Assert.assertEquals(userForm.getNickName(), userData.getNickName());

        // 列表
        PageData<UserData> userDataPage = find("/users", UserData.class, emptyMap());
        Assert.assertTrue(!userDataPage.isEmpty());
    }

    @After
    public void clean(){
        userRepository.deleteAll();
    }

}
