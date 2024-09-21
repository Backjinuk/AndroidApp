import axiosPost from '../../Util/AxiosUtil';

export const addChatRoomForOneToOne = async (
  userSeq: number,
  commuSeq: number = 0,
) => {
  const isPrivate = commuSeq === 0;
  const res = await axiosPost.post(
    '/chat/addChatRoom',
    JSON.stringify({
      chatters: [userSeq],
      type: isPrivate ? 'private' : 'public',
      commuSeq: commuSeq,
    }),
  );
  console.log(res.data);
  return res.data;
};

export const addChatRoomForGroup = (commuSeq: number) => {
  axiosPost.post(
    '/chat/addGroupRoom',
    JSON.stringify({
      commuSeq: commuSeq,
    }),
  );
};

export const addMember = (commuSeq: number) => {
  axiosPost.post(
    '/chat/addMember',
    JSON.stringify({
      commuSeq: commuSeq,
    }),
  );
};
