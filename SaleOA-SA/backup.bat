@echo off
echo 开始重要文件备份
echo 开始检测是否存在backup文件夹
set curDir=%~dp0
echo %curDir%
set backupDir=%curDir%backup
echo %backupDir%
if exist %backupDir% (
	echo backup文件夹已存在，准备开始复制重要文件
) else (
	echo backup文件夹不存在，开始创建文件夹
	md %backupDir%
	if exist %backupDir% (
		echo backup文件夹创建成功，准备开始复制重要文件
	) else (
		echo backup文件夹创建失败，请手动创建后重试
	)
)

set currTime=%date:~0,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%%time:~6,2%
echo 当前时间%currTime%
if exist %backupDir% (
	echo 开始复制重要文件
	xcopy %curDir%lib\json_encrypt.jar %backupDir%\ /y
	move %backupDir%\json_encrypt.jar %backupDir%\json_encrypt.jar_%currTime%
	echo 复制json_encrypt.jar成功，开始复制slf4j.jar
	xcopy %curDir%lib\slf4j.jar %backupDir%\ /y
	move %backupDir%\slf4j.jar %backupDir%\slf4j.jar_%currTime%
	echo 复制slf4j.jar成功
) else (
	echo backup文件夹不存在，请手动创建后重试
)

echo 备份完成
exit;