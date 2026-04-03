
# 纯小白最短路线

你现在不需要懂 Android 开发。

## 你要做的事
1. 把这个工程上传到你自己的 GitHub 仓库。
2. 把 libxposed 的 AAR 放到仓库 `libs/` 目录。
3. 打开 GitHub 仓库 → Actions → 运行 `DouPlus Modern Build`。
4. 默认先打 Debug 包。
5. 打包完成后，下载产物里的 APK。
6. 手机上安装 APK，并在 LSPosed 里勾选抖音作用域。

## 如果你没有 libxposed AAR
先不要强行打包。因为当前 Modern API 官方文档明确说明这套 API 仍在持续演进，很多项目会把依赖放到本地 Maven 或者直接把 AAR 放进仓库。没有这一步，CI 通常会卡在依赖阶段。

## 这个工程已经做了什么
- 多模块 Android Studio 工程
- Modern API 入口文件
- 当前抖音 38.2.0 / 380201 宿主分析
- 规则目录
- Room 数据库、配置层、备份层
- Settings / Video / Comment / Chat / Ward 安装链
- GitHub Actions 自动打包

## 还没被彻底做死的部分
- 当前抖音版本的真实 Hook 点
- 真机日志验证
- Resolver 的最终命中修正
