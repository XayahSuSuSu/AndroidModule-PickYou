# MaterialYouFileExplorer
[![GitHub release](https://img.shields.io/github/v/release/XayahSuSuSu/Android-MaterialYouFileExplorer?color=orange)](https://github.com/XayahSuSuSu/Android-MaterialYouFileExplorer/releases)  ![minSdk](https://img.shields.io/badge/minSdk-26-green) [![License](https://img.shields.io/github/license/XayahSuSuSu/Android-MaterialYouFileExplorer?color=ff69b4)](./LICENSE)

一个Material You风格的文件浏览器（选择器）第三方库。

使用这个第三方库来快速选择文件/文件夹。

## 特点
- 易于导入和使用。
- 高度可自定义化
- 支持选择文件或文件夹
- 支持过滤
- 支持选择文件/文件夹时管理文件/文件夹（删除、重命名）

## 截图

![Sample3](doc/images/Sample1.jpg "Sample1")

![Sample3](doc/images/Sample2.jpg "Sample2")

![Sample3](doc/images/Sample3.jpg "Sample3")

## 引入
1. 在 `settings.gradle` 中开启 `maven { url 'https://jitpack.io' }`
```
repositories {
        ......
        maven { url 'https://jitpack.io' }
    }
```
2. 引入MaterialYouFileExplorer
```
implementation 'com.github.XayahSuSuSu:Android-MaterialYouFileExplorer:1.3.1'
```

## 使用
1. 在 `onCreate()` 中初始化
```
val materialYouFileExplorer = MaterialYouFileExplorer().apply {
    initialize(this@MainActivity)
}
```
2. 打开Explorer Activity并且处理回调
```
materialYouFileExplorer.toExplorer(context) { path, isFile -> 
    // Code here
}
```
#### 自定义标题
```
materialYouFileExplorer.title = "Custom Title"
```

#### *Shell初始化
如果您在主项目中使用了`com.github.topjohnwu.libsu`依赖，请确保在初始化时添加了`FLAG_MOUNT_MASTER`标志
```
Shell.setDefaultBuilder(
    Shell.Builder.create()
        .setFlags(Shell.FLAG_MOUNT_MASTER or Shell.FLAG_REDIRECT_STDERR)
        .setTimeout(10)
)
```


## 样本
```
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val materialYouFileExplorer = MaterialYouFileExplorer().apply {
            initialize(this@MainActivity)
        }

        binding.filledButton.setOnClickListener {
            materialYouFileExplorer.apply {
                isFile = binding.radioButtonFile.isChecked
                title =
                    if (binding.checkBox.isChecked) binding.textInputEditTextTitle.text.toString() else "default"
                suffixFilter = ArrayList(binding.textInputEditTextFilter.text.toString().split(","))
                filterWhitelist = binding.checkBoxFilterWhitelist.isChecked
                defPath = "/storage/emulated/0/Download"

                toExplorer(it.context) { path, _ ->
                    binding.textInputEditText.setText(
                        path
                    )
                }
            }
        }
    }
}
```

## 鸣谢
- [libsu](https://github.com/topjohnwu/libsu)
- [PermissionX](https://github.com/guolindev/PermissionX)
- [MaterialFiles](https://github.com/zhanghai/MaterialFiles)
- [Coil](https://github.com/coil-kt/coil)
- [ActivityResultLauncher](https://github.com/DylanCaiCoding/ActivityResultLauncher)