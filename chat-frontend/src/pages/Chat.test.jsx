import { describe, it, expect, vi, afterEach } from 'vitest'
import { render, screen, fireEvent, cleanup } from '@testing-library/react'
import { MemoryRouter, Route, Routes } from 'react-router-dom'
import Chat from './Chat'

vi.mock('../api/axios', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
  },
}))

const api = await import('../api/axios')

afterEach(() => {
  cleanup()
  vi.clearAllMocks()
})

describe('Chat page', () => {
  it('shows a sent message in the chat after submitting', async () => {
    const initialMessages = []
    const messagesAfterSend = [
      { id: 1, sender: 'Alice', content: 'Hello' },
    ]

    api.default.get.mockImplementation((url) => {
      if (url === '/api/groups/1/messages') {
        return Promise.resolve({ data: initialMessages })
      }
      return Promise.resolve({ data: [] })
    })

    api.default.post.mockResolvedValue({ data: {} })

    render(
      <MemoryRouter initialEntries={['/groups/1']}>
        <Routes>
          <Route path="/groups/:groupId" element={<Chat />} />
        </Routes>
      </MemoryRouter>,
    )

    expect(await screen.findByText('Keine Nachrichten.')).toBeInTheDocument()

    api.default.get.mockResolvedValueOnce({ data: messagesAfterSend })

    fireEvent.change(screen.getByRole('textbox'), {
      target: { value: 'Hello' },
    })
    fireEvent.click(screen.getByText('Send'))

    expect(await screen.findByText('Hello')).toBeInTheDocument()
    // sender may be rendered with punctuation, use regex to match substring
    expect(screen.getByText(/Alice/)).toBeInTheDocument()
  })
})
