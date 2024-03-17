# DIDACTO-API

### 작업 규칙
   
1. 개발 시 디폴트 브랜치는 develop이다.
2. 새로운 요구사항에 대한 작업을 시작할 때 Issues 작성한 후, develop에서 브랜치를 분리하여 작업을 시작한다.

3. 이슈의 이름은 아래의 규칙을 따른다.
```
[FEAT] Some feature add and problem solve
```

4. 브랜치의 이름은 아래의 규칙을 따르며 생성한 이슈 번호를 포함시킨다.

```
type/#Issue No.
ex) feature/#3
```

5. 해당 이슈 브랜치 내부에서의 커밋은 메시지에 이슈 번호를 붙인다.
```
feat : add some (#3)
```

6. 브랜치를 develop에 병합할 때, Pull requests를 날린다. pull requests의 이름은 다음과 같이 명명한다.
```
[FEAT] Some feature add and problem solve (#3)
```
<br><hr><br>
### Commit message 

기본적으로 commit message 는 제목, 이슈번호, 본문, 꼬리말로 구성한다.
제목은 필수사항이며, 본문과 꼬리말은 선택사항이다.
```
<type>: <subject> (#이슈번호)

<body>

<footer>
```

| Type     | 내용                                        |
|----------|---------------------------------------------|
| feat     | 새로운 기능 추가, 기존의 기능을 요구 사항에 맞추어 수정  |
| fix      | 기능에 대한 버그 수정                         |
| build    | 빌드 관련 수정                               |
| chore    | 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore |
| ci       | CI 관련 설정 수정                             |
| docs     | 문서(주석) 수정                              |
| style    | 코드 스타일, 포맷팅에 대한 수정               |
| refactor | 기능의 변화가 아닌 코드 리팩터링 ex) 변수 이름 변경 |
| test     | 테스트 코드 추가/수정                         |
| release  | 버전 릴리즈                                  |