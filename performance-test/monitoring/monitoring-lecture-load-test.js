import http from "k6/http";
import { sleep, check } from 'k6';
import { generateTokens } from './auth.js';

const host = "http://localhost:8080";
const startLectureId = 0

export let options = {
    vus: 6,
    duration: '60s',
};

export function setup() {
    const tokens = generateTokens();
    return { tokens };
}

// 여러개의 강의가 동시에 진행되는 시나리오
// 강의 접속(SSE)은 수동으로 진행 필요
export default function ({tokens}) {
    const url = host + '/api/v1/monitoring/image/upload';
    const lectureId = startLectureId + __VU;  // __VU는 현재 VU의 고유 번호 (1부터 시작)

    const randomImage = images[Math.floor(Math.random() * images.length)]
    const randomToken = tokens[Math.floor(Math.random() * tokens.length)]

    console.info("lectureId: ", lectureId)

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
