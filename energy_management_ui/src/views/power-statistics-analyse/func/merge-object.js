// 将相同字段unitName的值放到同一数组中
export function mergeObject(data) {
    const arr = [];
    try {
        data.forEach((item) => {
            if (item.dataType !== 1) {
                for (let i = 0; i < arr.length; i++) {
                    if (arr[i].unitName === item.unitName) {
                        arr[i].children.push(item);
                        return;
                    }
                }
                arr.push({
                    unitName: item.unitName,
                    children: [item],
                });
            }
        });
        return arr;
    } catch (e) {
        return [];
    }
}
