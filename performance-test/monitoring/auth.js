import http from "k6/http";
import { check } from "k6";

const defaultHost = "http://localhost:8080";

export default function () {
    generateTokens(defaultHost, users);
}

// 여러 사용자에 대한 토큰을 생성하는 함수
export function generateTokens(host) {
    const tokens = [];
    console.info("users: ", users);
    for (let i = 0; i < users.length; i++) {
        const user = users[i];
        const token = generateToken(host, user.email, user.password);
        tokens.push(token);
    }

    return tokens;
}

// 토큰을 생성하는 함수
export function generateToken(host, email, password) {
    console.info("generateToken: ", email, password);

    if (host === undefined) {
        host = defaultHost;
    }

    const url = host + "/api/v1/auth/signin";
    const payload = JSON.stringify({
        email: email,
        password: password
    });

    const params = {
        headers: {
            "Content-Type": "application/json",
        },
    };

    // POST 요청을 보내고 응답에서 accessToken을 추출
    let res = http.post(url, payload, params);

    // 응답이 200 OK 인지 확인
    check(res, {
        '로그인 성공': (r) => r.status === 200,
    });

    return res.json('response.accessToken');
}

const users = JSON.parse(open('./users.json'));