@echo PhoneLibrary�� Compile ��.. (1/5)

@javac -sourcepath ./PhoneLibrary/src -d ./PhoneLibrary/bin ./PhoneLibrary/src/*.java ./PhoneLibrary/src/phoneLibrary/address/*.java ./PhoneLibrary/src/phoneLibrary/communication/*.java ./PhoneLibrary/src/phoneLibrary/io/*.java

@echo PhoneLibrary�� ���̺귯�� jar ���� ���� ��.. (2/5)

@jar cvf ./MyPhone/bin/PhoneLibrary.jar -C ./PhoneLibrary/bin/ .

@echo MyPhone (�׽�Ʈ ���ø����̼�) Compile ��.. (3/5)

@javac -sourcepath ./MyPhone/src -classpath ./MyPhone/bin/PhoneLibrary.jar;./MyPhone/src/DJNativeSwing.jar;./MyPhone/src/DJNativeSwing-SWT.jar;./MyPhone/src/DJNativeSwing-SWTDemo.jar -d ./MyPhone/bin ./MyPhone/src/*.java ./MyPhone/src/myOS/*.java ./MyPhone/src/myOS/application/*.java

@echo MyPhone (�׽�Ʈ ���ø����̼�) Jar ���� ���� ��.. (4/5)
del /s /q .\release
rmdir .\release
mkdir .\release
cd .\MyPhone\bin
jar xvf PhoneLibrary.jar
cd ..\..
jar cvfm ./release/MyPhone.jar ./MyPhone/MANIFEST.MF -C ./MyPhone/bin/ .

@echo MyPhone (�׽�Ʈ ���ø����̼�) ������ ���� ���� ��.. (5/5)

@xcopy /E /Y .\MyPhone\apps\* .\release\apps\*
@xcopy /E /Y .\MyPhone\data\* .\release\data\*
@xcopy /E /Y .\MyPhone\res\* .\release\res\*

@echo �Ϸ�Ǿ����ϴ�
@pause