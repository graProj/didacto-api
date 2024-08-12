import http from "k6/http";
import { sleep, check } from 'k6';
import { generateTokens } from './auth.js';

const host = "http://localhost:8080";
const lectureId = 248;

export let options = {
    vus: 5,
    duration: '5s',
};

export function setup() {
    const tokens = generateTokens();
    return { tokens };
}

// 하나의 강의에서 여러 명의 학생이 화면을 업로드하는 시나리오
export default function ({tokens}) {
    const url = host + '/api/v1/monitoring/image/upload';

    const randomImage = images[Math.floor(Math.random() * images.length)]
    const randomToken = tokens[Math.floor(Math.random() * tokens.length)]

    const payload = JSON.stringify({
        lectureId: lectureId,
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

const images = JSON.parse(open('./sample-image-base64.json'));
