@echo off
echo ��ʼ��Ҫ�ļ�����
echo ��ʼ����Ƿ����backup�ļ���
set curDir=%~dp0
echo %curDir%
set backupDir=%curDir%backup
echo %backupDir%
if exist %backupDir% (
	echo backup�ļ����Ѵ��ڣ�׼����ʼ������Ҫ�ļ�
) else (
	echo backup�ļ��в����ڣ���ʼ�����ļ���
	md %backupDir%
	if exist %backupDir% (
		echo backup�ļ��д����ɹ���׼����ʼ������Ҫ�ļ�
	) else (
		echo backup�ļ��д���ʧ�ܣ����ֶ�����������
	)
)

set currTime=%date:~0,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%%time:~6,2%
echo ��ǰʱ��%currTime%
if exist %backupDir% (
	echo ��ʼ������Ҫ�ļ�
	xcopy %curDir%lib\json_encrypt.jar %backupDir%\ /y
	move %backupDir%\json_encrypt.jar %backupDir%\json_encrypt.jar_%currTime%
	echo ����json_encrypt.jar�ɹ�����ʼ����slf4j.jar
	xcopy %curDir%lib\slf4j.jar %backupDir%\ /y
	move %backupDir%\slf4j.jar %backupDir%\slf4j.jar_%currTime%
	echo ����slf4j.jar�ɹ�
) else (
	echo backup�ļ��в����ڣ����ֶ�����������
)

echo �������
exit;