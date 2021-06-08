原神国服国际服切换工具（PC端）
================

这个是一个可以切换国服和国际服的小工具。

本工具可能并不会继续维护。

## 使用前须知

* 请不要使用除 Github 以外其他来源下载的文件。
* 本项目的初衷仅仅是为了节约双端带来的大量重复文件占用（25G）。
* 本项目内包含的所有游戏文件均未经任何修改。
* 本项目不会破坏任何游戏文件的完整性，因此不属于外挂。
* 如果你不信任这里给出的 `backups-x.x.zip` 可以查看[进阶操作](#进阶操作)。
* 注意 `backups-x.x.zip` 的版本，如果版本更新且未有相对应的 `backups-x.x.zip` 需自行查看[进阶操作](#进阶操作)提取文件。

## 使用方法

0. 建议备份整个游戏以免手滑误操作。
1. 下载最新的工具及备份差分包。
2. 解压到本地，注意格式为以下示例：

```
├─app
├─backups
├─runtime
├─genshin-imapct-server-switcher.exe
└─genshin-imapct-server-switcher.ico
```

3. 运行工具指定游戏目录（启动器目录）。
4. 点击【切换到。。。】即可完成。

## 进阶操作

进阶操作适用于当原神进行大版本更新后使用。

1. 从游戏目录下找到 `pkg_version` 并根据类型改名为 `pkg_version_cn` 或 `pkg_version_global`并复制到 `backups` 文件中。
2. 运行工具并点击 【对比与。。。的差异并备份】，国际服和国服都需要进行备份。

## License

```txt
Copyright 2021 Omico

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
