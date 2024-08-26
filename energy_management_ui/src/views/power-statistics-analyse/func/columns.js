export function getColumns(data, energyType,compareTxt, energyTypeOption) {
    const columns = [];
    try {
        columns[0] = {
            dataIndex: 'unitName',
            title: '组织机构名称',
            align: 'center',
            width: 140,
        };
        const energyTypes =
            energyType === null || energyType === ''
                ? []
                : energyType.split('/');
        let every = {};
        data[0].children.forEach((e) => {
            const isHave =
                energyTypes.length > 0
                    ? energyTypes.some((item) => item === e.energyType)
                    : true;
                
            if (isHave) {
                // let unit =  (e.energyUnit?'('+e.energyUnit+')':'')
                let curEnergy = energyTypeOption.filter(item => {
                  return item.energyValue === e.energyType
                })
                let unit = ''
                if (curEnergy.length > 0) {
                  unit = curEnergy[0].energyUnit ? '('+ curEnergy[0].energyUnit +')' : ''
                }
                every = {
                    dataIndex: `${Math.random()}`,
                    title:e.energyName,
                    align: 'center',
                    children: [
                        {
                            title: '本期值'+unit,
                            dataIndex: `currentValue${e.energyType}`,
                            align: 'center',
                            width: 100,
                        },
                        {
                            title: '同期值'+unit,
                            dataIndex: `oldValue${e.energyType}`,
                            align: 'center',
                            width: 100,
                        },
                        {
                            title: compareTxt+'(%)',
                            dataIndex: `ratio${e.energyType}`,
                            align: 'center',
                            width: 100,
                        },
                    ],
                };
                columns.push(every);
            }
        });
        return columns;
    } catch (e) {
        return [];
    }
}
