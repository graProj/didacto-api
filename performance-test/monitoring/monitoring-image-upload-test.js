import { SharedArray } from 'k6/data';
import http from "k6/http";
import { sleep, check } from 'k6';

// 하나의 강의에서 여러 명의 학생이 화면을 업로드하는 시나리오

export let options = {
    vus: 100,
    duration: '60s',
};

function getUrl() {
    return "http://localhost:8080";
}

export function setup() {
    const tokens = [];

    for (let i = 0; i < 6; i++) {
        const user = users[i]
        const token = generateToken(user.email, user.password)
        tokens.push(token)
    }

    return { tokens };

    function generateToken(email, password) {
        console.info("generateToken: ", email, password);

        const url = getUrl() + "/api/v1/auth/signin";
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
}

export default function ({tokens}) {
    const url = getUrl() + '/api/v1/monitoring/image/upload';

    const randomImage = images[Math.floor(Math.random() * images.length)]
    const randomToken = tokens[Math.floor(Math.random() * tokens.length)]

    const payload = JSON.stringify({
        lectureId: "248",
        encodedImageBase64: randomImage.base64,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + randomToken
        },
    };

    let res = http.post(url, payload, params);

    check(res, {
        '학생 화면 이미지 업로드': (r) => r.status === 200,
    });

    sleep(1);
}

// SharedArray must be called in the init context
const images = new SharedArray('images', function () {
    return JSON.parse(open('./sample-image-base64.json'));
});

const users = new SharedArray('users', function () {
    return JSON.parse(open('./users.json'));
});