# 构建与 CI 说明

这份工程已经内置 GitHub Actions 工作流，目标是让你在**不本地装 Android Studio**的情况下也能把模块打成 APK。

## 一、最省事的路线
1. 把工程整个上传到 GitHub 仓库
2. 准备 libxposed 依赖（本地 stub / 仓库内 AAR / 本地 Maven / 远程下载）
3. 在仓库 Actions 里运行 `DouPlus Modern Build`
4. 下载产物中的 APK

## 二、当前工作流支持的 libxposed 策略
工作流输入 `libxposed_strategy` 支持：
- `local-stub`：只用于先把工程编译链跑通，不保证运行时可用
- `bundled-aar`：把真实 AAR 放进仓库 `libs/`
- `local-maven`：在仓库中提供本地 Maven 目录
- `remote-download`：CI 里按 URL 下载 AAR

## 三、为什么默认是 local-stub
因为你现在最需要先验证：
- 工程结构是不是完整
- Gradle 能不能跑
- Actions 能不能出产物

等这些跑通之后，再把真实 `libxposed` AAR 放进去即可。

## 四、CI 里已经做掉的检查
- 检查 `META-INF/xposed/java_init.list`
- 检查 `META-INF/xposed/scope.list`
- 检查 `META-INF/xposed/module.prop`
- 自动准备 Android SDK 包
- 校验输出 APK 里是否包含 Modern API 入口文件

## 五、当前你最需要补的内容
- 真实 libxposed AAR
- 当前抖音版本的真实 Hook 点

## 六、这份工程的目标
不是“只做出一个能安装的空 APK”，而是做一个可以持续补 Resolver、补规则、最终变成可用 LSPosed Modern API 模块的仓库。
